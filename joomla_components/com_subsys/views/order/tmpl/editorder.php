<?php defined('_JEXEC') or die('Restricted access'); ?>

<form action="index.php" method="post" name="adminForm" id="adminForm">
<div class="col100">
	<fieldset class="adminform">
		<legend><?php echo JText::_( 'Pls fill in Order Details' ); ?></legend>

		<table class="admintable">
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Subscriber' ); ?>:
				</label>
			</td>
			<td>
				<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    dump($this->subscribers, "SUBS");
    foreach($this->subscribers as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name.'('.$value->sub_code.')'));
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'sub_code', 'onChange="document.adminForm.submit();"', 'value', 'text', $this->subcode);
				?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Code' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="order_code" id="order_code" size="32" maxlength="250" value="<?php echo $this->order->order_code;?>" />
			</td>
		</tr>
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'OrderID' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->order->order_id;?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Date' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="order_date" id="order_date" size="32" maxlength="250" value="<?php echo $this->order->order_date;?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('order_date','%Y-%m-%d');">
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Order Title' ); ?>:
				</label>
			</td>
			<td>
				<input class="inputbox" type="text" name="order_title" id="order_title" size="32" maxlength="250" value="<?php echo $this->order->order_title;?>" />
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Invoice Number' ); ?>:
				</label>
			</td>
			<td>
				<input class="textarea" type="text" name="order_invno" id="order_invno" size="32" maxlength="250" value="<?php echo $this->order->order_invno;?>" />
			</td>
		</tr>		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Invoice Amount' ); ?>:
				</label>
			</td>
			<td>
			<input class="textarea" type="text" name="order_invamt" id="order_invamt" size="32" maxlength="250" value="<?php echo $this->order->order_invamt;?>" />				
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Amount Paid?' ); ?>:
				</label>
			</td>
			<td>
			  <?php 
				$optionsPaid = array(JHTML::_('select.option','',JText::_('PLS CHOOSE OPTION')),
				  JHTML::_('select.option', 'Yes', JText::_('Yes')),				  
				  JHTML::_('select.option', 'No', JText::_('No'))				  
				);
    echo JHTML::_('select.genericlist', $optionsPaid, 'order_paid', 'class="inputbox"', 'value', 'text', $this->order->order_paid);
				?>
			</td>
		</tr>
		
		<tr>
			<td width="100" align="right" class="key">
				<label for="greeting">
					<?php echo JText::_( 'Created Date(Modified Date)' ); ?>:
				</label>
			</td>
			<td>
				<?php echo $this->order->cdate.'('.$this->order->mdate.')';?>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td width="100" align="right" class="key">				
			</td>
			<td>
			</td>
		</tr>
		
		<tr>
		  <table class="adminlist">
	    <thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'Subscription ID' ); ?>
			</th>
			<th width="20">
			<?php echo JText::_( 'Select Action' ); ?>
			</th>			
			<th>
				<?php echo JText::_( 'Publication' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscription Title' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issues' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Copies' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue from' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue To' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Start Date' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Exp Date' ); ?>
			</th>				
		</tr>
	</thead>
			<?php
	$k = 0;
	for ($i=0, $n=count( $this->subscriptions ); $i < $n; $i++)	{
		$row =& $this->subscriptions[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->subscription_id );	
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->subscription_id; ?>
				<input type="hidden" name="subscription_id<?php echo $i; ?>" value="<?php echo $row->subscription_id; ?>" />
			</td>
			<td>
				<?php 
				$actions = array(JHTML::_('select.option','',JText::_('ACTION?')),
				JHTML::_('select.option','Update',JText::_('Update')),
				JHTML::_('select.option','Delete',JText::_('Delete'))
				);
    echo JHTML::_('select.genericlist', $actions, 'action'.$i, '', 'value', 'text', '');
				?>
			</td>
			<td>
			<?php 
				$options = array();
 
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A PUBLICATION'));
    foreach($this->publications as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->pub_code, JText::_($value->pub_name.'('.$value->pub_code.')'));
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'pub_code'.$i, '', 'value', 'text', $row->pub_code);
				?>
			</td>
			<td>
			<input class="inputbox" type="text" name="subscription_title<?php echo $i; ?>" id="subscription_title<?php echo $i; ?>" size="15" maxlength="250" value="<?php echo $row->subscription_title; ?>" />				
			</td>
			<td>
			<input class="inputbox" type="text" name="num_issues<?php echo $i; ?>" id="num_issues<?php echo $i; ?>" size="8" maxlength="250" value="<?php echo $row->num_issues; ?>" />				
			</td>				
			<td>
			<input class="inputbox" type="text" name="num_copies<?php echo $i; ?>" id="num_copies<?php echo $i; ?>" size="8" maxlength="250" value="<?php echo $row->num_copies; ?>" />			
			</td>
			<td>
		<input class="inputbox" type="text" name="issue_from<?php echo $i; ?>" id="issue_from<?php echo $i; ?>" size="10" maxlength="250" value="<?php echo $row->issue_from; ?>" />		
			</td>
			<td>
				<input class="inputbox" type="text" name="issue_to<?php echo $i; ?>" id="issue_to<?php echo $i; ?>" size="10" maxlength="250" value="<?php echo $row->issue_to; ?>" />	
			</td>
			<td>
			<input class="textarea" type="text" name="start_date<?php echo $i; ?>" id="start_date<?php echo $i; ?>" size="15" maxlength="250" value="<?php echo $row->start_date; ?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('start_date<?php echo $i; ?>','%Y-%m-%d');">				
			</td>
			<td>
			<input class="textarea" type="text" name="exp_date<?php echo $i; ?>" id="exp_date<?php echo $i; ?>" size="15" maxlength="250" value="<?php echo $row->exp_date; ?>" />
				<input type="reset" class="button" value="..." onclick="return showCalendar('exp_date<?php echo $i; ?>','%Y-%m-%d');">		
			</td>			
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>	
  </table>
		</tr>
		
	</table>
	</fieldset>
</div>
<div class="clr"></div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="order_id" value="<?php echo $this->order->order_id; ?>" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="controller" value="order" />
<input type="hidden" name="countsubscription" value="<?php echo count( $this->subscriptions ); ?>">
</form>
