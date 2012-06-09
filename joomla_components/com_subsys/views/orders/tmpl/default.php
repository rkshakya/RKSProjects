<?php defined('_JEXEC') or die('Restricted access'); 
JHTML::_('behavior.tooltip');
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left" width="100%">
			<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    dump($this->subscribers, "SUBSCRIBERS");
    foreach($this->subscribers as $key=>$value) :
      ## Create $value ##
      #dump($value, "CATS");
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name).'('.$value->sub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'sub_code', 'onChange="document.adminForm.submit();"', 'value', 'text', $this->lists['subcode']);
				?>
			</td>
			
	</tr>
</table>
<?php if(!empty($this->lists['subcode'])) { ?>
<table width = '100%'>
  <tr><td align = 'left'><?php echo JText::_( 'Unique orders: ' ); ?> <?php echo $this->summary;?><?php echo JText::_( '(subscriptions: ' ); ?> <?php echo $this->lists['subcount'].')';?></td></tr>
</table>
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'Database ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
				<?php echo JText::_( 'Order Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Order Date' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Order Title' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Invoice Number' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Invoice Amount' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Amount Paid?' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Pub Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Pub Name' ); ?>
			</th>	
			<th>
				<?php echo JText::_( 'Sub Name' ); ?>
			</th>	
			<th>
				<?php echo JText::_( 'Num Issues' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Copies' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue From' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue To' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'StartDate' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'EndDate' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Created(Modified Date)' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->order_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=order&task=edit&cid[]='. $row->order_code );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->order_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->order_code; ?>
			</td>
			<td>
				<?php echo $row->order_date; ?>
			</td>
			<td>
				<?php echo $row->order_title; ?>
			</td>				
			<td>
				<?php echo $row->order_invno; ?>
			</td>
			<td>
				<?php echo $row->order_invamt; ?>
			</td>
			<td>
				<?php echo $row->order_paid; ?>
			</td>
			<td>
				<?php echo $row->pub_code; ?>
			</td>
			<td>
				<?php echo $row->pub_name; ?>
			</td>
			<td>
				<?php echo $row->sub_name; ?>
			</td>						
			<td>
				<?php echo $row->num_issues; ?>
			</td>
			<td>
				<?php echo $row->num_copies; ?>
			</td>
				<td>
				<?php echo $row->issue_from; ?>
			</td>
			<td>
				<?php echo $row->issue_to; ?>
			</td>	
			<td>
				<?php echo $row->start_date; ?>
			</td>
			<td>
				<?php echo $row->exp_date; ?>
			</td>
			<td>
				<?php echo $row->cdate.'('.$row->mdate.')'; ?>
			</td>			
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="18"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
	<?php } ?>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="order" />
</form>
