<?php defined('_JEXEC') or die('Restricted access'); 
JHTML::_('behavior.tooltip');
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left" width="100%">
				<?php echo JText::_( 'Filter' ); ?>:
				<input type="text" name="search" id="search" value="<?php echo htmlspecialchars($this->lists['search']);?>" class="text_area" onchange="document.adminForm.submit();" />
				<button onclick="this.form.submit();"><?php echo JText::_( 'Go' ); ?></button>
				<button onclick="document.getElementById('search').value='';this.form.submit();"><?php echo JText::_( 'Reset' ); ?></button>
			</td>
			
	</tr>
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
				<?php echo JText::_( 'Publication Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Publication Name' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Number of Issues' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Frequency' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Currency' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Rate 1' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Rate 2' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Rate 3' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Rate 4' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Principal' ); ?>
			</th>			
			<th>
				<?php echo JText::_( 'Category' ); ?>
			</th>						
			<th>
				<?php echo JText::_( 'Created Date' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Modified Date' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->pub_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=publication&task=edit&cid[]='. $row->pub_id );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->pub_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->pub_code; ?>
			</td>
			<td>
				<a href="<?php echo $link; ?>"><?php echo $row->pub_name; ?></a>
			</td>
			<td>
				<?php echo $row->pub_numissues; ?>
			</td>	
			<td>
				<?php echo $row->pub_frequency; ?>
			</td>
			<td>
				<?php echo $row->pub_currency; ?>
			</td>
			<td>
				<?php echo $row->pub_rate1; ?>
			</td>
			<td>
				<?php echo $row->pub_rate2; ?>
			</td>
			<td>
				<?php echo $row->pub_rate3; ?>
			</td>
			<td>
				<?php echo $row->pub_rate4; ?>
			</td>
			<td>
				<?php echo $row->pub_principal; ?>
			</td>			
			<td>
				<?php echo $row->cat_name; ?>
			</td>								
			<td>
				<?php echo $row->cdate; ?>
			</td>
			<td>
				<?php echo $row->mdate; ?>
			</td>
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="15"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="publication" />
</form>
